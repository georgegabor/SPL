package eu.matritel.spl.spl_webapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class StreamDTO {
    private String apiKey;
    private String sessionId;
    private String token;

    public StreamDTO(String apiKey, String sessionId, String token) {
        this.apiKey = apiKey;
        this.sessionId = sessionId;
        this.token = token;
    }
}
