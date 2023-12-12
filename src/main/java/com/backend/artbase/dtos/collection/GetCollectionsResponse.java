package com.backend.artbase.dtos.collection;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetCollectionsResponse {
    List<CollectionDto> collections;
}
