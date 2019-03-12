package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobBooleanTypeProperty
        extends CronJobTypeProperty
{

    public CronJobBooleanTypeProperty()
    {
        this(null);
    }

    public CronJobBooleanTypeProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public Object getValue()
    {
        return boolean.class.getSimpleName();
    }
}
