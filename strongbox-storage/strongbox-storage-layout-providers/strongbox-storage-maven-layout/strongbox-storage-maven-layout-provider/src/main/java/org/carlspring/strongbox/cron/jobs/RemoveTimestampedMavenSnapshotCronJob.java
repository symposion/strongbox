package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.repository.MavenRepositoryFeatures;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.storage.repository.RepositoryPolicyEnum;

import javax.inject.Inject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * @author Kate Novik.
 */
public class RemoveTimestampedMavenSnapshotCronJob
        extends JavaCronJob
{

    private static final String PROPERTY_STORAGE_ID = "storageId";

    private static final String PROPERTY_REPOSITORY_ID = "repositoryId";

    private static final String PROPERTY_BASE_PATH = "basePath";

    private static final String PROPERTY_NUMBER_TO_KEEP = "numberToKeep";

    private static final String PROPERTY_KEEP_PERIOD = "keepPeriod";

    private static final List<CronJobProperty> PROPERTIES = Arrays.asList(new CronJobProperty[]{
            new CronJobStorageIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_STORAGE_ID)))),
            new CronJobRepositoryIdAutocompleteProperty(new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_REPOSITORY_ID)))),
            new CronJobStringTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_BASE_PATH))),
            new CronJobIntegerTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_NUMBER_TO_KEEP))),
            new CronJobIntegerTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_KEEP_PERIOD))) });

    @Inject
    private MavenRepositoryFeatures mavenRepositoryFeatures;

    @Inject
    private ConfigurationManager configurationManager;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        String storageId = config.getProperty(PROPERTY_STORAGE_ID);
        String repositoryId = config.getProperty(PROPERTY_REPOSITORY_ID);
        String basePath = config.getProperty(PROPERTY_BASE_PATH);

        // The number of artifacts to keep
        int numberToKeep = config.getProperty(PROPERTY_NUMBER_TO_KEEP) != null ?
                           Integer.valueOf(config.getProperty(PROPERTY_NUMBER_TO_KEEP)) :
                           10;

        // The period to keep artifacts (the number of days)
        int keepPeriod = config.getProperty(PROPERTY_KEEP_PERIOD) != null ?
                         Integer.valueOf(config.getProperty(PROPERTY_KEEP_PERIOD)) :
                         30;

        if (storageId == null)
        {
            Map<String, Storage> storages = getStorages();
            for (String storage : storages.keySet())
            {
                removeTimestampedSnapshotArtifacts(storage, numberToKeep, keepPeriod);
            }
        }
        else if (repositoryId == null)
        {
            removeTimestampedSnapshotArtifacts(storageId, numberToKeep, keepPeriod);
        }
        else
        {
            mavenRepositoryFeatures.removeTimestampedSnapshots(storageId,
                                                               repositoryId,
                                                               basePath,
                                                               numberToKeep,
                                                               keepPeriod);
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
        return "Remove Timestamped Maven Snapshot Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Remove Timestamped Maven Snapshot Cron Job";
    }

    /**
     * To remove timestamped snapshot artifacts in repositories
     *
     * @param storageId    path of storage
     * @param numberToKeep the number of artifacts to keep
     * @param keepPeriod   the period to keep artifacts (the number of days)
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void removeTimestampedSnapshotArtifacts(String storageId,
                                                    int numberToKeep,
                                                    int keepPeriod)
            throws NoSuchAlgorithmException,
                   XmlPullParserException,
                   IOException
    {
        Map<String, Repository> repositories = getRepositories(storageId);

        repositories.forEach((repositoryId, repository) ->
                             {
                                 if (repository.getPolicy().equals(RepositoryPolicyEnum.SNAPSHOT.getPolicy()))
                                 {
                                     try
                                     {
                                         mavenRepositoryFeatures.removeTimestampedSnapshots(storageId,
                                                                                            repositoryId,
                                                                                            null,
                                                                                            numberToKeep,
                                                                                            keepPeriod);
                                     }
                                     catch (IOException e)
                                     {
                                         logger.error(e.getMessage(), e);
                                     }
                                 }
                             });
    }

    private Map<String, Storage> getStorages()
    {
        return configurationManager.getConfiguration().getStorages();
    }

    private Map<String, Repository> getRepositories(String storageId)
    {
        return getStorages().get(storageId).getRepositories();
    }

}
