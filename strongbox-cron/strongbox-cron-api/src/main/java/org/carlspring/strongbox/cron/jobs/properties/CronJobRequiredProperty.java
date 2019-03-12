package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobRequiredProperty
        extends CronJobProperty
{

    public CronJobRequiredProperty()
    {
        this(null);
    }

    public CronJobRequiredProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public String getKey()
    {
        return "required";
    }

    @Override
    public Object getValue()
    {
        return true;
    }
}
