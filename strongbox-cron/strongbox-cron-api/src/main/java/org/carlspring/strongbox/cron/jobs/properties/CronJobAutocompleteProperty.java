package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public abstract class CronJobAutocompleteProperty
        extends CronJobProperty
{

    public CronJobAutocompleteProperty()
    {
        this(null);
    }

    public CronJobAutocompleteProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public String getKey()
    {
        return "autocomplete";
    }

}
