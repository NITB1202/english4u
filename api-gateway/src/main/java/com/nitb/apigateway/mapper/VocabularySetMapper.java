package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.userservice.grpc.UserResponse;
import com.nitb.vocabularyservice.grpc.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static CreateVocabularySetResponseDto toCreateVocabularySetResponseDto(CreateVocabularySetResponse set, VocabularyWordsResponse words) {
        List<VocabularyWordSummaryResponseDto> summaries = words.getWordsList().stream()
                .map(VocabularyWordMapper::toVocabularyWordSummaryResponseDto)
                .toList();

        return CreateVocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .createdBy(UUID.fromString(set.getCreatedBy()))
                .createdAt(LocalDateTime.parse(set.getCreateAt()))
                .words(summaries)
                .build();
    }

    public static VocabularySetSummaryResponseDto toVocabularySetSummaryResponseDto(VocabularySetSummaryResponse set) {
        return VocabularySetSummaryResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .version(set.getVersion())
                .wordCount(set.getWordCount())
                .build();
    }

    public static VocabularySetDetailResponseDto toVocabularySetDetailResponseDto(VocabularySetDetailResponse set, UserResponse createdBy, UserResponse updatedBy) {
        return VocabularySetDetailResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .createdByName(createdBy.getName())
                .createdByAvatarUrl(createdBy.getAvatarUrl())
                .createdAt(LocalDateTime.parse(set.getCreateAt()))
                .name(set.getName())
                .version(set.getVersion())
                .wordCount(set.getWordCount())
                .updatedByName(updatedBy.getName())
                .updatedByAvatarUrl(updatedBy.getAvatarUrl())
                .updatedAt(LocalDateTime.parse(set.getUpdateAt()))
                .isDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularySetsPaginationResponseDto toVocabularySetsPaginationResponseDto(VocabularySetsResponse sets) {
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

    public static UpdateVocabularySetNameResponseDto toUpdateVocabularySetNameResponseDto(UpdateVocabularySetResponse set) {
        return UpdateVocabularySetNameResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .version(set.getVersion())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updateAt(LocalDateTime.parse(set.getUpdateAt()))
                .build();
    }

    public static UpdateVocabularySetResponseDto toUpdateVocabularySetResponseDto(UpdateVocabularySetResponse set, VocabularyWordsResponse words) {
        List<VocabularyWordSummaryResponseDto> summaries = words.getWordsList().stream()
                .map(VocabularyWordMapper::toVocabularyWordSummaryResponseDto)
                .toList();

        return UpdateVocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .version(set.getVersion())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updateAt(LocalDateTime.parse(set.getUpdateAt()))
                .words(summaries)
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

    public static VocabularySetStatisticResponseDto toVocabularySetStatisticResponseDto(VocabularySetStatisticResponse statistic) {
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
