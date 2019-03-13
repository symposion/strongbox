package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.CronJobProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author Yougeshwar
 */
public class MyTask
        extends JavaCronJob
{

    @Override
    public void executeTask(CronTaskConfigurationDto config)
    {
        logger.debug("Executed successfully.");
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return Collections.emptyList();
    }

    @Override
    public String getName()
    {
        return "My Task";
    }

    @Override
    public String getDescription()
    {
        return "My Task";
    }

}
