package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.config.MavenIndexerEnabledCondition;
import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.repository.IndexedMavenRepositoryFeatures;

import javax.inject.Inject;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.core.env.Environment;

/**
 * @author Kate Novik
 * @author carlspring
 */
public class DownloadRemoteMavenIndexCronJob
        extends JavaCronJob
{

    public static final String STRONGBOX_DOWNLOAD_INDEXES = "strongbox.download.indexes";

    private static final String PROPERTY_STORAGE_ID = "storageId";

    private static final String PROPERTY_REPOSITORY_ID = "repositoryId";

    private static final List<CronJobProperty> PROPERTIES = Arrays.asList(new CronJobProperty[]{
            new CronJobStorageIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_STORAGE_ID)))),
            new CronJobRepositoryIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_REPOSITORY_ID)))) });

    @Inject
    private IndexedMavenRepositoryFeatures features;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        logger.debug("Executing DownloadRemoteIndexCronJob.");

        String storageId = config.getProperty(PROPERTY_STORAGE_ID);
        String repositoryId = config.getProperty(PROPERTY_REPOSITORY_ID);

        features.downloadRemoteIndex(storageId, repositoryId);
    }

    @Override
    public boolean enabled(CronTaskConfigurationDto configuration,
                           Environment env)
    {
        if (!super.enabled(configuration, env))
        {
            return false;
        }

        boolean mavenIndexerEnabled = Boolean.parseBoolean(
                env.getProperty(MavenIndexerEnabledCondition.MAVEN_INDEXER_ENABLED));
        if (!mavenIndexerEnabled)
        {
            return false;
        }

        String storageId = configuration.getProperty(PROPERTY_STORAGE_ID);
        String repositoryId = configuration.getProperty(PROPERTY_REPOSITORY_ID);

        boolean shouldDownloadIndexes = shouldDownloadAllRemoteRepositoryIndexes();
        boolean shouldDownloadRepositoryIndex = shouldDownloadRepositoryIndex(storageId, repositoryId);

        return shouldDownloadIndexes || shouldDownloadRepositoryIndex;
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return PROPERTIES;
    }

    @Override
    public String getName()
    {
        return "Download Remote Maven Index Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Download Remote Maven Index Cron Job";
    }

    public static boolean shouldDownloadAllRemoteRepositoryIndexes()
    {
        return System.getProperty(STRONGBOX_DOWNLOAD_INDEXES) == null ||
               Boolean.parseBoolean(System.getProperty(STRONGBOX_DOWNLOAD_INDEXES));
    }

    public static boolean shouldDownloadRepositoryIndex(String storageId,
                                                        String repositoryId)
    {
        return (System.getProperty(STRONGBOX_DOWNLOAD_INDEXES + "." + storageId + "." + repositoryId) == null ||
                Boolean.parseBoolean(System.getProperty(STRONGBOX_DOWNLOAD_INDEXES + "." + storageId + "."
                                                        + repositoryId)))
               &&
               isIncludedDespiteWildcard(storageId, repositoryId);
    }

    public static boolean isIncludedDespiteWildcard(String storageId,
                                                    String repositoryId)
    {
        return // is excluded by wildcard
                !Boolean.parseBoolean(System.getProperty(STRONGBOX_DOWNLOAD_INDEXES + "." + storageId + ".*")) &&
                // and is explicitly included
                Boolean.parseBoolean(System.getProperty(STRONGBOX_DOWNLOAD_INDEXES + "." + storageId + "."
                                                        + repositoryId));
    }

}
