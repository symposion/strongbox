package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobNamedProperty
        extends CronJobProperty
{

    private final String name;

    public CronJobNamedProperty(String name)
    {
        this(null, name);
    }

    public CronJobNamedProperty(CronJobProperty property,
                                String name)
    {
        super(property);
        this.name = name;
    }

    @Override
    public String getKey()
    {
        return "name";
    }

    @Override
    public Object getValue()
    {
        return name;
    }
}
