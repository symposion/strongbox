package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public abstract class CronJobTypeProperty
        extends CronJobProperty
{

    public CronJobTypeProperty()
    {
        this(null);
    }

    public CronJobTypeProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public String getKey()
    {
        return "type";
    }
}
