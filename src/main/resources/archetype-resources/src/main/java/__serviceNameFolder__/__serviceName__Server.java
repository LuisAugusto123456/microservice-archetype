#set( $dollar = '$' )
package ${package}.${serviceNameFolder.replace('/','.')};

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = { "${package}.${serviceNameFolder.replace('/','.')}", "${package}.common" })
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class,
		DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class ${serviceName}Server {

	private static final Logger SERVER_LOGGER = LogManager.getLogger(${serviceName}Server.class);
	private static final String ENV_TARGET = "envTarget";

	@Configuration
	@Profile("dev")
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:dev}/config/config-${dollar}{envTarget:dev}.properties" })
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:dev}/secrets/config-${dollar}{envTarget:dev}.properties" })
	static class ConfigDev {
		@Bean
		InitializingBean init() {
			return () -> SERVER_LOGGER.info("Enviroment: Dev");
		}
	}

	@Configuration
	@Profile("uat")
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:uat}/config/config-${dollar}{envTarget:uat}.properties" })
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:uat}/secrets/config-${dollar}{envTarget:uat}.properties" })
	static class ConfigUat {
		@Bean
		InitializingBean init() {
			return () -> SERVER_LOGGER.info("Enviroment: uat");
		}
	}

	@Configuration
	@Profile("prod")
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:prod}/config/config-${dollar}{envTarget:prod}.properties" })
	@PropertySource({ "${dollar}{propertiesDir}/${dollar}{envTarget:prod}/secrets/config-${dollar}{envTarget:prod}.properties" })
	static class ConfigProd {
		@Bean
		InitializingBean init() {
			return () -> SERVER_LOGGER.info("Enviroment: Prod");
		}
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(${serviceName}Server.class);
		application.setAdditionalProfiles(
				System.getProperty(ENV_TARGET) == null ? System.getenv(ENV_TARGET) : System.getProperty(ENV_TARGET));
		application.run(args);
	}
}