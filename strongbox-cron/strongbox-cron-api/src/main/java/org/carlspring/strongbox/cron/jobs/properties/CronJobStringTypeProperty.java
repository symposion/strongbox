package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobStringTypeProperty
        extends CronJobTypeProperty
{

    public CronJobStringTypeProperty()
    {
        this(null);
    }

    public CronJobStringTypeProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public Object getValue()
    {
        return String.class.getSimpleName();
    }
}
