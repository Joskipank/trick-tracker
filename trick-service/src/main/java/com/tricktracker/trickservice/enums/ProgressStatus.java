package com.tricktracker.trickservice.enums;

public enum ProgressStatus {
    PLANNED,      // Запланировано
    IN_PROGRESS,  // В процессе изучения (регулярные попытки)
    LEARNED,      // Освоен (уверенное приземление, но требует концентрации)
    CONSISTENT    // Стабилен (выполнение на автомате, "в любой линии")
}
