package uk.ac.ebi.spot.goci.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.spot.goci.model.Association;

import java.util.Collection;
import java.util.List;


/**
 * Created by emma on 26/11/14.
 *
 * @author emma
 *         <p>
 *         Repository accessing Association entity object
 */
@RepositoryRestResource
public interface AssociationRepository extends JpaRepository<Association, Long> {
    Collection<Association> findByStudyId(long studyId);

    @RestResource(exported = false)
    Collection<Association> findByStudyId(long studyId, Sort sort);

    List<Association> findByStudyIdAndLastUpdateDateIsNotNullOrderByLastUpdateDateDesc(Long studyId);

    Collection<Association> findByLociStrongestRiskAllelesSnpId(long snpId);

    @RestResource(exported = false)
    List<Association> findByStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull();

    @RestResource(exported = false)
    List<Association> findByStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Sort sort);

    Page<Association> findByStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Pageable pageable);

    @RestResource(exported = false)
    List<Association> findByLociStrongestRiskAllelesSnpIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Long snpId);

    @RestResource(exported = false)
    List<Association> findByLociStrongestRiskAllelesSnpIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Sort sort,
            Long snpId);

    Page<Association> findByLociStrongestRiskAllelesSnpIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Pageable pageable,
            Long snpId);

    @RestResource(exported = false)
    List<Association> findByStudyDiseaseTraitIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Long diseaseTraitId);

    @RestResource(exported = false)
    List<Association> findByStudyDiseaseTraitIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Sort sort,
            Long diseaseTraitId);

    Page<Association> findByStudyDiseaseTraitIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Pageable pageable,
            Long diseaseTraitId);

    @RestResource(exported = false)
    List<Association> findByEfoTraitsIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Long efoTraitId);

    @RestResource(exported = false)
    List<Association> findByEfoTraitsIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Sort sort,
            Long efoTraitId);

    Page<Association> findByEfoTraitsIdAndStudyHousekeepingCatalogPublishDateIsNotNullAndStudyHousekeepingCatalogUnpublishDateIsNull(
            Pageable pageable,
            Long efoTraitId);
}
