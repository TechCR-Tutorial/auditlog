/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package auditlog.conf;

import org.springframework.context.ApplicationContext;

public class LocalSpringContext {

    private static ApplicationContext context;

    static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * Returns the bean instance for the given id.
     */
    public static Object getBean(String _beanId) {
        return context.getBean(_beanId);
    }

    public String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }
}