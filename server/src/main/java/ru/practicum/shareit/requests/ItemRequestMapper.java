package ru.practicum.shareit.requests;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto result = new ItemRequestDto();
        result.setId(itemRequest.getId());
        result.setDescription(itemRequest.getDescription());
        result.setRequesterId(itemRequest.getRequester().getId());
        result.setCreated(itemRequest.getCreatedAt());
        result.setItems(itemRequest.getItems());
        return result;
    }
}
