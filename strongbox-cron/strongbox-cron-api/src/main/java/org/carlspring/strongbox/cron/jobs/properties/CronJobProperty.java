package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public abstract class CronJobProperty
{

    private final CronJobProperty property;

    public CronJobProperty(CronJobProperty property)
    {
        this.property = property;
    }

    public abstract String getKey();

    public abstract Object getValue();

}
