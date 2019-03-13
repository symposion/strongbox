package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.CronJobProperty;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author carlspring
 */
public class OneTimeExecutionCronJob
        extends JavaCronJob
{

    int runs = 1;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        System.out.println("The one-time task has run " + runs + " times.");

        assertFalse(runs > 1, "Failed to execute in single run mode.");

        runs++;
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return Collections.emptyList();
    }

    @Override
    public String getName()
    {
        return "One Time Execution Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "One Time Execution Cron Job";
    }

    public int getRuns()
    {
        return runs;
    }

}

