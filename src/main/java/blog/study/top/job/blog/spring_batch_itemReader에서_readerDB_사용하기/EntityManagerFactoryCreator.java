package blog.study.top.job.blog.spring_batch_itemReader에서_readerDB_사용하기;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class EntityManagerFactoryCreator {
	private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

	private final JpaProperties properties;
	private final HibernateProperties hibernateProperties;
	private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProvider;
	private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;
	private final DataSource dataSource;
	private final String packages;
	private final String persistenceUnit;

	@Builder
	public EntityManagerFactoryCreator(JpaProperties properties, HibernateProperties hibernateProperties, ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProvider,
			EntityManagerFactoryBuilder entityManagerFactoryBuilder, DataSource dataSource, String packages, String persistenceUnit) {
		this.properties = properties;
		this.hibernateProperties = hibernateProperties;
		this.metadataProvider = metadataProvider;
		this.entityManagerFactoryBuilder = entityManagerFactoryBuilder;
		this.dataSource = dataSource;
		this.packages = packages;
		this.persistenceUnit = persistenceUnit;
	}

	public LocalContainerEntityManagerFactoryBean create() {
		Map<String, Object> vendorProperties = getVendorProperties();
		customizeVendorProperties(vendorProperties);
		return entityManagerFactoryBuilder
				.dataSource(this.dataSource)
				.packages(packages)
				.properties(vendorProperties)
				.persistenceUnit(persistenceUnit)
				.mappingResources(getMappingResources())
				.build();
	}

	private String[] getMappingResources() {
		List<String> mappingResources = this.properties.getMappingResources();
		return (!ObjectUtils.isEmpty(mappingResources) ? StringUtils.toStringArray(mappingResources) : null);
	}

	private Map<String, Object> getVendorProperties() {
		return new LinkedHashMap<>(this.hibernateProperties.determineHibernateProperties(
				this.properties.getProperties(),
				new HibernateSettings()
		));
	}

	private void customizeVendorProperties(Map<String, Object> vendorProperties) {
		if (!vendorProperties.containsKey(PROVIDER_DISABLES_AUTOCOMMIT)) {
			configureProviderDisablesAutocommit(vendorProperties);
		}
	}

	private void configureProviderDisablesAutocommit(Map<String, Object> vendorProperties) {
		if (iSDataSourcesAutoCommitDisabled()) {
			log.info("Hikari auto-commit: false");
			vendorProperties.put(PROVIDER_DISABLES_AUTOCOMMIT, "true");
		}
	}

	private boolean iSDataSourcesAutoCommitDisabled() {
		DataSourcePoolMetadataProvider poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(metadataProvider.getIfAvailable());
		DataSourcePoolMetadata poolMetadata = poolMetadataProvider.getDataSourcePoolMetadata(this.dataSource);
		return poolMetadata != null && Boolean.FALSE.equals(poolMetadata.getDefaultAutoCommit());
	}
}
