package com.tricktracker.trickservice.enums;

public enum ProgressStatus {
    NOT_STARTED, // Не входит в планы
    PLANNED,      // Запланировано
    IN_PROGRESS,  // В процессе изучения (регулярные попытки)
    LEARNED,      // Освоен (уверенное приземление, но требует концентрации)
    CONSISTENT    // Стабилен (выполнение на автомате, "в любой линии")
}
