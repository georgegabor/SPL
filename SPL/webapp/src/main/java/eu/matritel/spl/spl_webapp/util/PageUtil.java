package eu.matritel.spl.spl_webapp.util;


import eu.matritel.spl.spl_webapp.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {
    @Autowired
    private SecurityService securityService;

    public static Map<String, String> mapValues(String pageTitle, String pagePath, String pageSection) {

        Map<String, String> viewValues = new HashMap<>();

        viewValues.put("page_title", pageTitle);
        viewValues.put("page_path", pagePath);
        viewValues.put("page_section", pageSection);

        return viewValues;
    }
}
