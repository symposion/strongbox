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

    @Inject
    private RepositoryManagementService repositoryManagementService;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        String storageId = config.getProperty("storageId");
        String repositoryId = config.getProperty("repositoryId");

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
        return Arrays.asList(new CronJobProperty[]{
                new CronJobStorageIdAutocompleteProperty(new CronJobStringTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("storageId")))),
                new CronJobRepositoryIdAutocompleteProperty(new CronJobStringTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("repositoryId")))) });
    }

}
