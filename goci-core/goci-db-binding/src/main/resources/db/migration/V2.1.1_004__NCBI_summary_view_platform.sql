/*

################################################################################
Create a view table that joins across all the salient tables to produce a single
summary view over all the data in the catalogue that can be used to export
data to NCBI, including the new format platform fields

Designed for execution with Flyway database migrations tool.

author:  Dani Welter
date:    Mar 21st 2016
version: 2.1.1.004
################################################################################

*/
--------------------------------------------------------
-- Generate view
--------------------------------------------------------

CREATE OR REPLACE VIEW NCBI_CATALOG_SUMMARY_VIEW (
  ID,
  STUDY_ADDED_DATE,
  PUBMED_ID,
  AUTHOR,
  PUBLICATION_DATE,
  JOURNAL,
  LINK,
  STUDY,
  DISEASE_TRAIT,
  INITIAL_SAMPLE_DESCRIPTION,
  REPLICATE_SAMPLE_DESCRIPTION,
  REGION,
  REPORTED_GENE,
  STRONGEST_SNP_RISK_ALLELE,
  SNP_RSID,
  SNP_ID,
  RISK_ALLELE_FREQUENCY,
  P_VALUE_MANTISSA,
  P_VALUE_EXPONENT,
  P_VALUE_QUALIFIER,
  OR_BETA,
  CI,
  CI_QUALIFIER,
  PLATFORM,
  QUALIFIER,
  SNP_COUNT,
  IMPUTED,
  STUDY_DESIGN_COMMENT,
  CNV,
  ASSOCIATION_ID,
  STUDY_ID,
  CATALOG_PUBLISH_DATE,
  CATALOG_UNPUBLISH_DATE,
  EFO_TRAIT,
  EFO_URI)
  AS SELECT ROWNUM, V.* FROM
  (SELECT
  h.STUDY_ADDED_DATE,
  s.PUBMED_ID,
  s.AUTHOR,
  s.PUBLICATION_DATE,
  s.PUBLICATION AS JOURNAL,
  CONCAT('http://europepmc.org/abstract/MED/', s.PUBMED_ID) AS LINK,
  s.TITLE AS STUDY,
  dt.TRAIT AS DISEASE_TRAIT,
  s.INITIAL_SAMPLE_SIZE AS INITIAL_SAMPLE_DESCRIPTION,
  s.REPLICATE_SAMPLE_SIZE AS REPLICATE_SAMPLE_DESCRIPTION,
  r.NAME AS REGION,
  rg.GENE_NAME AS REPORTED_GENE,
  ra.RISK_ALLELE_NAME AS STRONGEST_SNP_RISK_ALLELE,
  snp.RS_ID AS SNP_RS_ID,
  snp.ID AS SNP_ID,
  a.RISK_FREQUENCY AS RISK_ALLELE_FREQUENCY,
  a.PVALUE_MANTISSA AS P_VALUE_MANTISSA,
  a.PVALUE_EXPONENT AS P_VALUE_EXPONENT,
  a.PVALUE_TEXT AS P_VALUE_QUALIFIER,
  a.OR_PER_COPY_NUM AS OR_BETA,
  a.OR_PER_COPY_RANGE AS CI,
  a.OR_PER_COPY_UNIT_DESCR AS CI_QUALIFIER,
  p.MANUFACTURER,
  s.QUALIFIER,
  s.SNP_COUNT,
  s.IMPUTED,
  s.STUDY_DESIGN_COMMENT,
  s.CNV,
  a.ID AS ASSOCIATION_ID,
  s.ID AS STUDY_ID,
  h.CATALOG_PUBLISH_DATE,
  h.CATALOG_UNPUBLISH_DATE,
  e.TRAIT,
  e.URI
  FROM STUDY s
  JOIN HOUSEKEEPING h ON h.ID = s.HOUSEKEEPING_ID
  JOIN CURATION_STATUS cs ON h.CURATION_STATUS_ID = cs.ID
  LEFT JOIN STUDY_DISEASE_TRAIT sdt ON sdt.STUDY_ID = s.ID
  LEFT JOIN DISEASE_TRAIT dt ON dt.ID = sdt.DISEASE_TRAIT_ID
  LEFT JOIN ASSOCIATION a ON a.STUDY_ID = s.ID
  LEFT JOIN ASSOCIATION_LOCUS al ON al.ASSOCIATION_ID = a.ID
  LEFT JOIN LOCUS_RISK_ALLELE lra ON lra.LOCUS_ID = al.LOCUS_ID
  LEFT JOIN RISK_ALLELE ra ON ra.ID = lra.RISK_ALLELE_ID
  LEFT JOIN RISK_ALLELE_SNP ras ON ras.RISK_ALLELE_ID = lra.RISK_ALLELE_ID
  LEFT JOIN SINGLE_NUCLEOTIDE_POLYMORPHISM snp ON snp.ID = ras.SNP_ID
  LEFT JOIN SNP_LOCATION ls ON ls.SNP_ID = snp.ID
  LEFT JOIN LOCATION loc ON ls.LOCATION_ID = loc.id
  LEFT JOIN REGION r ON r.ID = loc.REGION_ID
  LEFT JOIN AUTHOR_REPORTED_GENE arg ON arg.LOCUS_ID = al.LOCUS_ID
  LEFT JOIN GENE rg ON rg.ID = arg.REPORTED_GENE_ID
  LEFT JOIN ASSOCIATION_EFO_TRAIT ae on ae.ASSOCIATION_ID = a.ID
  LEFT JOIN STUDY_PLATFORM sp ON sp.STUDY_ID = s.ID
  LEFT JOIN PLATFORM p ON p.ID = sp.PLATFORM_ID
  LEFT JOIN EFO_TRAIT e on e.ID = ae.EFO_TRAIT_ID
  ORDER BY s.PUBLICATION_DATE DESC) V
