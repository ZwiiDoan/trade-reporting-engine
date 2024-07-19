package per.duyd.interview.tre.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static per.duyd.interview.tre.TestUtil.readTestResourceFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import per.duyd.interview.tre.TestUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  public void postAndValidateResponse(
      String requestFile, String responseFile,
      Class<?> responseClass,
      HttpStatus expectedResponseStatus, String path) throws Exception {
    //When:
    MvcResult mvcResult = mockMvc.perform(post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(readTestResourceFile(requestFile)))
        .andExpect(status().is(expectedResponseStatus.value()))
        .andReturn();

    //Then:
    assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass))
        .isEqualTo(
            objectMapper.readValue(TestUtil.readTestResourceFile(responseFile), responseClass));
  }

  public void getAndValidateResponse(
      String responseFile,
      Class<?> responseClass,
      HttpStatus expectedResponseStatus, String path) throws Exception {
    //When:
    MvcResult mvcResult = mockMvc.perform(get(path))
        .andExpect(status().is(expectedResponseStatus.value()))
        .andReturn();

    //Then:
    assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass))
        .isEqualTo(
            objectMapper.readValue(TestUtil.readTestResourceFile(responseFile), responseClass));

  }

}
