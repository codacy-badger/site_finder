package com.finder;

import com.finder.model.Site;
import com.finder.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        SiteService service =  context.getBean(SiteService.class);

        if (args.length > 0) {
            String searchKeyword = args[0];
            List<Site> sites = null;

            try {
                sites = service.searchSites(searchKeyword);

            } catch (IOException e) {
                logger.info(e.toString());
            }

            for(Site site : sites) {
                logger.info(site.getId());
                logger.info(site.getName());
            }
        }

    }
}
