package com.jrfom.icelotto.config;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.jrfom.gw2.ApiClient;
import com.jrfom.icelotto.util.ImageDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@EnableJpaRepositories("com.jrfom.icelotto.repository")
@EnableTransactionManagement
@PropertySources({
  @PropertySource("classpath:application-${spring.profiles.active}.properties")
})
public class ApplicationContextBeans {
  @Autowired
  private Environment env;

  @Autowired
  private DataSourceConfig dataSourceConfig;

  @Bean
  public ServletContextTemplateResolver templateResolver() {
    ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
    resolver.setPrefix("/WEB-INF/templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode("HTML5");

    // We need to see the latest and greatest during development.
    if (this.isDevEnvironment()) {
      resolver.setCacheable(false);
    }

    return resolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(this.templateResolver());
    engine.addDialect(new DataAttributeDialect());
    engine.addDialect(new SpringSecurityDialect());

    return engine;
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(this.templateEngine());

    return viewResolver;
  }

  @Bean
  public ApiClient apiClient() {
    return new ApiClient();
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(this.dataSourceConfig.dataSource());
  }

  @Bean
  public DataSourceTransactionManager jdbcTransactionManager() {
    return new DataSourceTransactionManager(this.dataSourceConfig.dataSource());
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ImageDownloader imageDownloader() {
    return new ImageDownloader(
      this.env.getRequiredProperty("application.store.path")
    );
  }

  private boolean isDevEnvironment() {
    boolean result = false;

    String[] activeProfiles = this.env.getActiveProfiles();
    for (String profile : activeProfiles) {
      if (profile.equals("dev")) {
        result = true;
        break;
      }
    }

    return result;
  }
}