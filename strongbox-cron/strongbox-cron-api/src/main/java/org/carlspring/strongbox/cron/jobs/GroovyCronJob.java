package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.CronJobNamedProperty;
import org.carlspring.strongbox.cron.jobs.properties.CronJobProperty;
import org.carlspring.strongbox.cron.jobs.properties.CronJobRequiredProperty;
import org.carlspring.strongbox.cron.jobs.properties.CronJobStringTypeProperty;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

/**
 * @author carlspring
 * @author Yougeshwar
 */
public class GroovyCronJob
        extends AbstractCronJob
{

    private static final String PATH_PROPERTY = "script.path";

    private static final List<CronJobProperty> PROPERTIES = Arrays.asList(
            new CronJobProperty[]{ new CronJobStringTypeProperty(
                    new CronJobRequiredProperty(new CronJobNamedProperty(PATH_PROPERTY))) });

    @Override
    @SuppressFBWarnings(value = "DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED")
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        try
        {
            Class scriptClass = new GroovyClassLoader().parseClass(
                    new GroovyCodeSource(Paths.get(getScriptPath(config)).toUri()));
            Object scriptInstance = scriptClass.getConstructor().newInstance();
            //noinspection unchecked
            scriptClass.getDeclaredMethod("execute", new Class[]{}).invoke(scriptInstance);
        }
        catch (IOException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
        {
            logger.error("IOException: ", e);
        }
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return PROPERTIES;
    }

    @Override
    public String getName()
    {
        return "Groovy Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Groovy Cron Job";
    }

    public String getScriptPath(CronTaskConfigurationDto configuration)
    {
        return configuration.getRequiredProperty(PATH_PROPERTY);
    }

}
