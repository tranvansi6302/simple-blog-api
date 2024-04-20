package com.simpleblogapi.simpleblogapi.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private int page;
    private int limit;
    @JsonProperty("total_page")
    private int totalPage;
}
