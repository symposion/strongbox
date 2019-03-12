package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobOptionalProperty
        extends CronJobProperty
{

    public CronJobOptionalProperty()
    {
        this(null);
    }

    public CronJobOptionalProperty(CronJobProperty property)
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
        return false;
    }
}
