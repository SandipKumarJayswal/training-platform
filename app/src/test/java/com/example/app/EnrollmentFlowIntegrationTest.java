package com.example.app;

import com.example.notifications.core.notification.NotificationRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void publishCourseThenEnrollProducesNotification() throws Exception {
        String courseResponse = mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Intro to Spring","capacity":1,"city":"Berlin"}
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long courseId = ((Number) JsonPath.read(courseResponse, "$.id")).longValue();

        mockMvc.perform(post("/api/v1/courses/" + courseId + "/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(true));

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Jane Doe","email":"jane@example.com","courseId":%d}
                                """.formatted(courseId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        mockMvc.perform(get("/api/v1/enrollments").param("courseId", courseId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        assertThat(notificationRepository.findAll())
                .anyMatch(n -> n.getRecipient().equals("jane@example.com") && n.getChannel().equals("EMAIL"));
    }

    @Test
    void secondEnrollmentIsRejectedWhenCourseIsAtCapacity() throws Exception {
        String courseResponse = mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Full Course","capacity":1,"city":"Munich"}
                                """))
                .andReturn().getResponse().getContentAsString();
        Long courseId = ((Number) JsonPath.read(courseResponse, "$.id")).longValue();

        mockMvc.perform(post("/api/v1/courses/" + courseId + "/publish"));

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"First","email":"first@example.com","courseId":%d}
                                """.formatted(courseId)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Second","email":"second@example.com","courseId":%d}
                                """.formatted(courseId)))
                .andExpect(status().isConflict());
    }
}