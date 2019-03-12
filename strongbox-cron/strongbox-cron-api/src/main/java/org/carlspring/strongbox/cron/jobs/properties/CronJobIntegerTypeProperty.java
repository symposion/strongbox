package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobIntegerTypeProperty
        extends CronJobTypeProperty
{

    public CronJobIntegerTypeProperty()
    {
        this(null);
    }

    public CronJobIntegerTypeProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public Object getValue()
    {
        return int.class.getSimpleName();
    }
}
