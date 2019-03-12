package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.services.ChecksumService;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;

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
public class RegenerateChecksumCronJob
        extends JavaCronJob
{

    @Inject
    private ChecksumService checksumService;

    @Inject
    private ConfigurationManager configurationManager;

    @Override
    public void executeTask(CronTaskConfigurationDto config)
            throws Throwable
    {
        String storageId = config.getProperty("storageId");
        String repositoryId = config.getProperty("repositoryId");
        String basePath = config.getProperty("basePath");

        /**
         * The values of forceRegeneration are:
         * - true  - to re-write existing checksum and to regenerate missing checksum,
         * - false - to regenerate missing checksum only
         */
        boolean forceRegeneration = Boolean.valueOf(config.getProperty("forceRegeneration"));

        if (storageId == null)
        {
            Map<String, Storage> storages = getStorages();
            for (String storage : storages.keySet())
            {
                regenerateRepositoriesChecksum(storage, forceRegeneration);
            }
        }
        else if (repositoryId == null)
        {
            regenerateRepositoriesChecksum(storageId, forceRegeneration);
        }
        else
        {
            checksumService.regenerateChecksum(storageId, repositoryId, basePath, forceRegeneration);
        }
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return Arrays.asList(new CronJobProperty[]{
                new CronJobStorageIdAutocompleteProperty(new CronJobStringTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("storageId")))),
                new CronJobRepositoryIdAutocompleteProperty(new CronJobStringTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("repositoryId")))),
                new CronJobBooleanTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("forceRegeneration"))),
                new CronJobStringTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("basePath"))) });
    }

    /**
     * To regenerate artifact's checksum in repositories
     *
     * @param storageId         path of storage
     * @param forceRegeneration true - to re-write existing checksum and to regenerate missing checksum,
     *                          false - to regenerate missing checksum only
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void regenerateRepositoriesChecksum(String storageId,
                                                boolean forceRegeneration)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        Map<String, Repository> repositories = getRepositories(storageId);

        for (String repositoryId : repositories.keySet())
        {
            checksumService.regenerateChecksum(storageId, repositoryId, null, forceRegeneration);
        }
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
