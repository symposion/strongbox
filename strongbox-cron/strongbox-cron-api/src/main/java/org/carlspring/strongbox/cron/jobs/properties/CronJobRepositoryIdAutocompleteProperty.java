package org.carlspring.strongbox.cron.jobs.properties;

/**
 * @author Przemyslaw Fusik
 */
public class CronJobRepositoryIdAutocompleteProperty extends CronJobAutocompleteProperty
{
    public CronJobRepositoryIdAutocompleteProperty()
    {
        this(null);
    }

    public CronJobRepositoryIdAutocompleteProperty(CronJobProperty property)
    {
        super(property);
    }

    @Override
    public Object getValue()
    {
        return "repositoryId";
    }
}
