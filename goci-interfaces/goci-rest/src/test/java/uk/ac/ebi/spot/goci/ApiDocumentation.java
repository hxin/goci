package uk.ac.ebi.spot.goci;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dani Welter
 * @date 16/11/16
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDocumentation {

    @Rule
    public final JUnitRestDocumentation
            restDocumentation = new JUnitRestDocumentation("src/main/asciidoc/generated-snippets");

    private RestDocumentationResultHandler restDocumentationResultHandler;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.restDocumentationResultHandler = document("{method-name}",
                                                       preprocessRequest(prettyPrint()),
                                                       preprocessResponse(prettyPrint())
        );

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.restDocumentationResultHandler)
                .build();
    }


    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("index"));
    }

    @Test
    public void pageExample () throws Exception {

//        this.restDocumentationResultHandler.document(
//                responseFields(
//                        fieldWithPath("_links").description("<<resources-page-links,Links>> to other resources"),
//                        fieldWithPath("_embedded").description("The list of resources"),
//                        fieldWithPath("page.size").description("The number of resources in this page"),
//                        fieldWithPath("page.totalElements").description("The total number of resources"),
//                        fieldWithPath("page.totalPages").description("The total number of pages"),
//                        fieldWithPath("page.number").description("The page number")
//                ),
//                links(halLinks(),
//                        linkWithRel("self").description("This resource list"),
//                        linkWithRel("first").description("The first page in the resource list"),
//                        linkWithRel("next").description("The next page in the resource list"),
//                        linkWithRel("prev").description("The previous page in the resource list"),
//                        linkWithRel("last").description("The last page in the resource list")
//                )
//
//        );

        this.mockMvc.perform(get("/api/studies?page=1&size=1"))
                .andDo(this.restDocumentationResultHandler.document(
                        responseFields(
                                fieldWithPath("_links").description("<<resources-page-links,Links>> to other resources"),
                                fieldWithPath("_embedded").description("The list of resources"),
                                fieldWithPath("page.size").description("The number of resources in this page"),
                                fieldWithPath("page.totalElements").description("The total number of resources"),
                                fieldWithPath("page.totalPages").description("The total number of pages"),
                                fieldWithPath("page.number").description("The page number")
                        ),
                        links(halLinks(),
                              linkWithRel("self").description("This resource list"),
                              linkWithRel("first").description("The first page in the resource list"),
                              linkWithRel("next").description("The next page in the resource list"),
                              linkWithRel("prev").description("The previous page in the resource list"),
                              linkWithRel("last").description("The last page in the resource list"),
                              linkWithRel("profile").description("Study resource profile"),
                              linkWithRel("search").description("Available search methods")
                        )

                ))
                .andExpect(status().isOk());
    }



    @Test
    public void errorExample() throws Exception {
        this.mockMvc
                .perform(get("/error")
                                 .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                                 .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
                                              "/notes")
                                 .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                                              "The tag 'http://localhost:8080/gwas/rest/api/studies/123' does not exist"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is(notNullValue())))
                .andDo(document("error-example",
                                responseFields(
                                        fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                        fieldWithPath("message").description("A description of the cause of the error"),
                                        fieldWithPath("path").description("The path to which the request was made"),
                                        fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                        fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }


    @Test
    public void apiExample () throws Exception {

//        this.restDocumentationResultHandler.document(
//                responseFields(
//                        fieldWithPath("_links").description("<<resources-ontologies-links,Links>> to other resources")
//                ),
//                links(halLinks(),
//                        linkWithRel("studies").description("Link to the studies in the GWAS Catalog"),
//                        linkWithRel("associations").description("Link to all the associations in the GWAS Catalog"),
//                        linkWithRel("ethnicities").description("Link to all the ancestry entries in the GWAS Catalog"),
//                        linkWithRel("efoTraits").description("Link to all the EFO traits in the GWAS Catalog"),
//                        linkWithRel("genes").description("Link to all the genes in the GWAS Catalog")
//                        )
//        );
        this.mockMvc.perform(get("/api").accept(MediaType.APPLICATION_JSON))
                .andDo(this.restDocumentationResultHandler.document(
                        responseFields(
                                fieldWithPath("_links").description("<<resources-ontologies-links,Links>> to other resources")
                        ),
                        links(halLinks(),
                              linkWithRel("studies").description("Link to the studies in the GWAS Catalog"),
                              linkWithRel("associations").description("Link to all the associations in the GWAS Catalog"),
                              linkWithRel("ancestries").description("Link to all the ancestry entries in the GWAS Catalog"),
                              linkWithRel("efoTraits").description("Link to all the EFO traits in the GWAS Catalog"),
                              linkWithRel("genes").description("Link to all the genes in the GWAS Catalog"),
                              linkWithRel("regions").description("Link to all the chromsome regions in the GWAS Catalog"),
                              linkWithRel("countries").description("Link to all the countries in the GWAS Catalog"),
                              linkWithRel("entrezGenes").description("Link to all the Entrez gene IDs in the GWAS Catalog"),
                              linkWithRel("ensemblGenes").description("Link to all the Ensembl gene IDs in the GWAS Catalog"),
                              linkWithRel("mappingMetadatas").description("Link to the genomic mapping metadata in the GWAS Catalog"),
                              linkWithRel("platforms").description("Link to all the sequencing platforms in the GWAS Catalog"),
                              linkWithRel("genomicContexts").description("Link to all the genomic contexts in the GWAS Catalog"),
                              linkWithRel("riskAlleles").description("Link to all the risk alleles in the GWAS Catalog"),
                              linkWithRel("diseaseTraits").description("Link to all the disease traits in the GWAS Catalog"),
                              linkWithRel("locations").description("Link to all the bp locations in the GWAS Catalog"),
                              linkWithRel("loci").description("Link to all the association-risk allele locus link objects in the GWAS Catalog"),
                              linkWithRel("singleNucleotidePolymorphisms").description("Link to all the SNPs in the GWAS Catalog"),
                              linkWithRel("profile").description("Link to the API profile")
                              )))
                .andExpect(status().isOk());
    }


    @Test
    public void studiesListExample () throws Exception {

        this.mockMvc.perform(get("/api/studies").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void studiesExample() throws Exception {
//        this.restDocumentationResultHandler.document(
//                pathParameters(
//                        parameterWithName("study_id").description("The id of the study in the GWAS Catalog")),
//
//                responseFields(
//                        fieldWithPath("_links").description("<<studies-links,Links>> to other resources"),
//                        fieldWithPath("pubmedId").description("The pubmed ID for the study"),
//                        fieldWithPath("accessionId").description("The study's GWAS Catalog accession ID"),
//                        fieldWithPath("fullPvalueSet").description("Whether full summary statistics are available for this study")
//                ),
//                links(halLinks(),
//                      linkWithRel("self").description("This study"),
//                      linkWithRel("ancestries").description("<<overview-pagination,Paginated>> list of <<ancestries-resources,ancestries>> in this study"),
//                      linkWithRel("diseaseTrait").description("<<overview-pagination,Paginated>> list of <<diseaseTrait-resources,disease traits>> in this study"),
//                      linkWithRel("efoTraits").description("<<overview-pagination,Paginated>> list of <<efoTraits-resources,EFO traits>> in this study"),
//                      linkWithRel("platforms").description("<<overview-pagination,Paginated>> list of <<platforms-resources,platforms>> in this study"),
//                      linkWithRel("associations").description("<<overview-pagination,Paginated>> list of <<associations-resources,associations>> in this study")
//
//                      )
//
//        );

        this.mockMvc.perform(get("/api/studies/{study_id}", "5993").accept(MediaType.APPLICATION_JSON))
                .andDo( this.restDocumentationResultHandler.document(
                        pathParameters(
                                parameterWithName("study_id").description("The id of the study in the GWAS Catalog")),

                        responseFields(
                                fieldWithPath("_links").description("<<studies-links,Links>> to other resources"),
                                fieldWithPath("pubmedId").description("The pubmed ID for the study"),
                                fieldWithPath("accessionId").description("The study's GWAS Catalog accession ID"),
                                fieldWithPath("fullPvalueSet").description("Whether full summary statistics are available for this study"),
                                fieldWithPath("author").description("The study's first author"),
                                fieldWithPath("publicationDate").description("The study's publication date"),
                                fieldWithPath("publication").description("The study's journal"),
                                fieldWithPath("title").description("The study title"),
                                fieldWithPath("initialSampleSize").description("Initial sample description"),
                                fieldWithPath("replicateSampleSize").description("Replication sample description"),
                                fieldWithPath("gxe").description("Whether the study investigates a gene-environment interaction"),
                                fieldWithPath("gxg").description("Whether the study investigates a gene-gene interaction"),
                                fieldWithPath("genomewideArray").description("Whether a genome-wide array was used"),
                                fieldWithPath("targetedArray").description("Whether a targted array was used"),
                                fieldWithPath("snpCount").description("Number of SNPs passing QC"),
                                fieldWithPath("qualifier").description("Qualifier of number of SNPs passing QC (eg >)"),
                                fieldWithPath("imputed").description("Whether SNPs were imputed"),
                                fieldWithPath("pooled").description("Whether samples were pooled"),
                                fieldWithPath("studyDesignComment").description("Any other relevant study design information")
                                ),
                        links(halLinks(),
                              linkWithRel("self").description("This study"),
                              linkWithRel("study").description("This study"),
                              linkWithRel("ancestries").description("<<overview-pagination,Paginated>> list of <<ancestries-resources,ancestries>> in this study"),
                              linkWithRel("diseaseTrait").description("<<overview-pagination,Paginated>> list of <<diseaseTrait-resources,disease traits>> in this study"),
                              linkWithRel("efoTraits").description("<<overview-pagination,Paginated>> list of <<efoTraits-resources,EFO traits>> in this study"),
                              linkWithRel("platforms").description("<<overview-pagination,Paginated>> list of <<platforms-resources,platforms>> in this study"),
                              linkWithRel("associations").description("<<overview-pagination,Paginated>> list of <<associations-resources,associations>> in this study")

                        )

                ))
                .andExpect(status().isOk());
    }

    @Test
    public void associationsExample() throws Exception {

    }

    @Test
    public void locationsExample() throws Exception {

    }

    @Test
    public void regionsExample() throws Exception {

    }

    @Test
    public void genomicContextsExample() throws Exception {

    }

    @Test
    public void efoTraitsExample() throws Exception {

    }

    @Test
    public void mappingMetadatasExample() throws Exception {

    }

    @Test
    public void riskAllelesExample() throws Exception {

    }

    @Test
    public void diseaseTraitsExample() throws Exception {

    }

    @Test
    public void countriesExample() throws Exception {

    }

    @Test
    public void singleNucleotidePolymorphismsExample() throws Exception {

    }

    @Test
    public void ancestriesExample() throws Exception {

    }

    @Test
    public void entrezGenesExample() throws Exception {

    }

    @Test
    public void ensemblGenesExample() throws Exception {

    }

    @Test
    public void genesExample() throws Exception {

    }


    @Test
    public void lociExample() throws Exception {

    }

    @Test
    public void platformsExample() throws Exception {

    }

}
