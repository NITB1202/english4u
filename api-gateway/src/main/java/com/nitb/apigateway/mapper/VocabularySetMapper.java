package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.vocabularyservice.grpc.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static VocabularySetWithWordsResponseDto toVocabularySetWithWordsResponse(CreateVocabularySetResponse set, VocabularyWordsResponse words) {
        List<VocabularyWordResponseDto> dto = words != null ?
                words.getWordsList().stream()
                .map(VocabularyWordMapper::toVocabularyWordResponseDto)
                .toList()
                : null;

        return VocabularySetWithWordsResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .wordCount(dto != null ? dto.size() : 0)
                .createdBy(UUID.fromString(set.getCreatedBy()))
                .createAt(LocalDateTime.parse(set.getCreateAt()))
                .words(dto)
                .build();
    }

    public static VocabularySetSummaryResponseDto toVocabularySetSummaryResponseDto(VocabularySetSummaryResponse set) {
        return VocabularySetSummaryResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .wordCount(set.getWordCount())
                .build();
    }

    public static VocabularySetDetailResponseDto toVocabularySetDetailResponseDto(VocabularySetDetailResponse set) {
        return VocabularySetDetailResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .createdBy(UUID.fromString(set.getCreatedBy()))
                .createdAt(LocalDateTime.parse(set.getCreateAt()))
                .name(set.getName())
                .wordCount(set.getWordCount())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updatedAt(LocalDateTime.parse(set.getUpdateAt()))
                .isDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularySetsPaginationResponseDto vocabularySetsPaginationResponseDto(VocabularySetsResponse sets) {
        List<VocabularySetSummaryResponseDto> setsResponse = sets.getSetsList()
                .stream()
                .map(VocabularySetMapper::toVocabularySetSummaryResponseDto)
                .toList();

        return VocabularySetsPaginationResponseDto.builder()
                .sets(setsResponse)
                .totalItems(sets.getTotalItems())
                .totalPages(sets.getTotalPages())
                .build();
    }

    public static UpdateVocabularySetResponseDto toUpdateVocabularySetResponseDto(UpdateVocabularySetResponse set) {
        return UpdateVocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updateAt(LocalDateTime.parse(set.getUpdateAt()))
                .build();
    }

    public static DeleteVocabularySetResponseDto toDeleteVocabularySetResponseDto(DeleteVocabularySetResponse set) {
        return DeleteVocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updateAt(LocalDateTime.parse(set.getUpdateAt()))
                .isDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularySetStatisticResponseDto toVocabularySetStatisticResponseDto(VocabularySetStatistic statistic) {
        return VocabularySetStatisticResponseDto.builder()
                .time(statistic.getTime())
                .publishedCount(statistic.getCount())
                .build();
    }

    public static VocabularySetStatisticsResponseDto toVocabularySetStatisticsResponseDto(CountPublishedVocabularySetsResponse statistics) {
        List<VocabularySetStatisticResponseDto> dto = statistics.getStatisticsList().stream()
                .map(VocabularySetMapper::toVocabularySetStatisticResponseDto)
                .toList();

        return VocabularySetStatisticsResponseDto.builder()
                .statistics(dto)
                .build();
    }
}
