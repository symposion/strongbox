package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.services.RepositoryManagementService;

import javax.inject.Inject;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author Kate Novik.
 * @author Przemyslaw Fusik
 */
public class ClearRepositoryTrashCronJob
        extends JavaCronJob
{

    private static final String PROPERTY_STORAGE_ID = "storageId";

    private static final String PROPERTY_REPOSITORY_ID = "repositoryId";

    private static final List<CronJobProperty> PROPERTIES = Arrays.asList(new CronJobProperty[]{
            new CronJobStorageIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_STORAGE_ID)))),
            new CronJobRepositoryIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_REPOSITORY_ID)))) });

    @Inject
    private RepositoryManagementService repositoryManagementService;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        String storageId = config.getProperty(PROPERTY_STORAGE_ID);
        String repositoryId = config.getProperty(PROPERTY_REPOSITORY_ID);

        if (storageId == null && repositoryId == null)
        {
            repositoryManagementService.deleteTrash();
        }
        else
        {
            repositoryManagementService.deleteTrash(storageId, repositoryId);
        }
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return PROPERTIES;
    }

    @Override
    public String getName()
    {
        return "Clear Repository Trash Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Clear Repository Trash Cron Job";
    }

}
