package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.repository.NpmRepositoryFeatures;

import javax.inject.Inject;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.core.env.Environment;

/**
 * @author Sergey Bespalov
 */
public class FetchRemoteNpmChangesFeedCronJob
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
    private NpmRepositoryFeatures features;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        String storageId = config.getProperty(PROPERTY_STORAGE_ID);
        String repositoryId = config.getProperty(PROPERTY_REPOSITORY_ID);

        features.fetchRemoteChangesFeed(storageId, repositoryId);
    }

    public static String calculateJobName(String storageId,
                                          String repositoryId)
    {
        return String.format("Fetch Remote Changes feed for %s:%s", storageId, repositoryId);
    }

    @Override
    public boolean enabled(CronTaskConfigurationDto configuration,
                           Environment env)
    {
        if (!super.enabled(configuration, env))
        {
            return false;
        }

        return shouldDownloadRemoteChangesFeed();
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return PROPERTIES;
    }

    @Override
    public String getName()
    {
        return "Fetch Remote Npm Changes Feed Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Fetch Remote Npm Changes Feed Cron Job";
    }

    public static boolean shouldDownloadRemoteChangesFeed()
    {
        return System.getProperty("strongbox.npm.remote.changes.enabled") == null ||
               Boolean.parseBoolean(System.getProperty("strongbox.npm.remote.changes.enabled"));
    }
}
