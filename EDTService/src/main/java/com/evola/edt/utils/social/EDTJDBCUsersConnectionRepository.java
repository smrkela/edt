package com.evola.edt.utils.social;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

/**
 * Workaround klasa jer nece da se setuje signUp konektor.
 * @author smrkela
 *
 */
public class EDTJDBCUsersConnectionRepository extends JdbcUsersConnectionRepository {

	
	@Inject
	public void setConnector(ConnectionSignUp connector){
		
		super.setConnectionSignUp(connector);
	}
	
	public EDTJDBCUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		super(dataSource, connectionFactoryLocator, textEncryptor);
		// TODO Auto-generated constructor stub
	}

}
