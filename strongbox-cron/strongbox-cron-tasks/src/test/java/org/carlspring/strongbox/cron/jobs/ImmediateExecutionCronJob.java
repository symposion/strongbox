package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.CronJobProperty;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author carlspring
 */
public class ImmediateExecutionCronJob
        extends JavaCronJob
{


    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        System.out.println("ImmediateExecutionCronJob executed!");
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return Collections.emptyList();
    }

    @Override
    public String getName()
    {
        return "Immediate Execution Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Immediate Execution Cron Job";
    }

}

