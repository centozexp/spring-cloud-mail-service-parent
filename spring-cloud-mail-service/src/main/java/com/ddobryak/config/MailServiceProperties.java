package com.ddobryak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.mail.service")
public class MailServiceProperties {
	
	private Mail mail = new Mail();
	private Wait wait = new Wait();
	private Pool pool = new Pool();
	
	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Wait getWait() {
		return wait;
	}

	public void setWait(Wait wait) {
		this.wait = wait;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	@Override
	public String toString() {
		return "MailServiceProperties [mail=" + mail + ", wait=" + wait + ", pool=" + pool + ", getMail()=" + getMail()
				+ ", getWait()=" + getWait() + ", getPool()=" + getPool() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	public static class Mail {
		private String host;
		private int port;
		private String username;
		private String password;
		private boolean auth;
		private boolean ssl;
		private String protocol;
		private boolean allow8bitmime;
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isAuth() {
			return auth;
		}
		public void setAuth(boolean auth) {
			this.auth = auth;
		}
		public boolean isSsl() {
			return ssl;
		}
		public void setSsl(boolean ssl) {
			this.ssl = ssl;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public boolean isAllow8bitmime() {
			return allow8bitmime;
		}
		public void setAllow8bitmime(boolean allow8bitmime) {
			this.allow8bitmime = allow8bitmime;
		}
		
		@Override
		public String toString() {
			return "Mail [host=" + host + ", port=" + port + ", username=" + username + ", password=" + password
					+ ", auth=" + auth + ", ssl=" + ssl + ", protocol=" + protocol + ", allow8bitmime=" + allow8bitmime
					+ "]";
		}
	}
	
	public static class Wait {
		private boolean await = true;
		private int timeout = 10;
		
		public boolean isAwait() {
			return await;
		}
		public void setAwait(boolean await) {
			this.await = await;
		}
		public int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
		
		@Override
		public String toString() {
			return "Wait [await=" + await + ", timeout=" + timeout + "]";
		}
	}
	
	public static class Pool {
		private int min = 5;
		private int max = 10;
		private int capacity = 1000;
		
		public int getMin() {
			return min;
		}
		public void setMin(int min) {
			this.min = min;
		}
		public int getMax() {
			return max;
		}
		public void setMax(int max) {
			this.max = max;
		}
		public int getCapacity() {
			return capacity;
		}
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
		
		@Override
		public String toString() {
			return "Pool [min=" + min + ", max=" + max + ", capacity=" + capacity + "]";
		}
	}


}