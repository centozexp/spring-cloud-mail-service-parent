package com.ddobryak.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.ddobryak.service.MailSenderService;
import com.ddobryak.service.MailService;
import com.ddobryak.utils.EntityConverter;
import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;
import com.ddobryak.model.MailStatus;
import com.ddobryak.service.MailQueueService;

public class MailQueueServiceImpl implements MailQueueService {

	private static final Logger log = Logger.getLogger(MailQueueServiceImpl.class.getName());

	private final BlockingQueue<Mail> mails = new LinkedBlockingQueue<>();
	private final AtomicBoolean shutdown = new AtomicBoolean(false);
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private MailSenderService mailSenderService;
	
	@Autowired
	private MailService mailService;

	@Autowired
	private EntityConverter converter;

	/**
	 * add mail to send to queue
	 * @param mail - mail to send
	 * @return 
	 */
	@Override
	public Mail add(MailDTO mailDTO) {
		log.log(Level.INFO, "Adding email to queue, %s", mailDTO);
		
		Mail tmp = converter.convert(mailDTO);
		tmp.setStatus(MailStatus.SENDING);
		
		Mail res = mailService.save(tmp);
		addToQueue(res);
		
		return res;
	}
	
	@PostConstruct
	private void start() {
		taskExecutor.execute(() -> {
			try {
				while (!shutdown.get()) {
					List<Mail> mailToSend = new ArrayList<>();
					List<Future<Boolean>> mailSendResult = new ArrayList<>();

					// send all mails from queue or wait
					log.info("Start sending mails or wait for new one");
					sendMails(mailToSend, mailSendResult);
					// wait for sending complete
					log.info("Wait until mails send");
					waitSendDone(mailSendResult);
					// add failed mails to queue again
					int failedCount = processSendMails(mailToSend, mailSendResult);
					
					// sleep if there are mails that failed to send to smtp servers
					if(failedCount > 0) {
						log.info(String.format("There are %d error during sending mails to server. Sleep for 10 seconds", failedCount));
						Thread.sleep(10000);
					}
				}
			} catch (InterruptedException e) {
				log.info(e.getMessage());
				Thread.currentThread().interrupt();
			}
		});

		mailService.findSendingMails().forEach(this::addToQueue);
	}
	
	@PreDestroy
	private void shutdown() {
		shutdown.set(true);
	}
	
	private void addToQueue(Mail mail) {
		try {
			this.mails.put(mail);
		} catch (InterruptedException e) {
			log.info(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * send all mails from queue
	 * @param mailToSend - array of mails to send
	 * @param mailSendResult - array of mail sending result
	 * @throws InterruptedException
	 */
	private void sendMails(List<Mail> mailToSend, List<Future<Boolean>> mailSendResult) throws InterruptedException {
		// get mail or wait
		Mail mail = mails.take();
		
		// get all mails from queue and send them
		do {
			log.log(Level.INFO, "Start sending email, %s", mail);
			Future<Boolean> res = mailSenderService.send(mail);

			mailToSend.add(mail);
			mailSendResult.add(res);
		} while((mail = mails.poll()) != null);
	}

	/**
	 * wait until all mails send
	 * @param mailSendResult
	 * @throws InterruptedException
	 */
	private void waitSendDone(List<Future<Boolean>> mailSendResult) throws InterruptedException {
		boolean done;
		do {
			done = mailSendResult.stream().allMatch(Future::isDone);
			
			if(!done) {
				Thread.sleep(10);
			}
		} while(!done);
	}
	
	/**
	 * check sending result. If sending failed then add mail to queue again
	 * @param mailToSend - array of mails to send
	 * @param mailSendResult - array of sending result
	 * @return count of failed mails
	 * @throws InterruptedException
	 */
	private int processSendMails(List<Mail> mailToSend, List<Future<Boolean>> mailSendResult) throws InterruptedException {
		int failedCount = 0;
		
		for(int i = 0; i < mailSendResult.size(); ++i) {
			boolean sendResult = processSendMail(mailToSend.get(i), mailSendResult.get(i));
			
			if(!sendResult) {
				++ failedCount;
			}
		}
		
		return failedCount;
	}
	
	private boolean processSendMail(Mail mail, Future<Boolean> sendResult) throws InterruptedException {
		boolean res = false;
		
		try {
			Boolean tmp = sendResult.get();
			
			if(tmp) {
				res = true;
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "Error during sending emails", e);
		}
		
		if(res) {
			mail.setStatus(MailStatus.SUCCESS);
			mailService.save(mail);
		} else {
			mail.setAttempts(mail.getAttempts() - 1);
			
			if(mail.getAttempts() > 0) {
				mails.put(mail);
			} else {
				mail.setStatus(MailStatus.FAILED);
				mailService.save(mail);
			}
		}
		
		return res;
	}
}
