package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobStorageIdAutocompleteProperty
        extends CronJobAutocompleteProperty
{

    public CronJobStorageIdAutocompleteProperty()
    {
        this(null);
    }

    public CronJobStorageIdAutocompleteProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public Object getValue()
    {
        return "storageId";
    }
}
