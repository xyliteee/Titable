package com.Xyliteee.titable;

import java.time.LocalDate;

public class StaticSource {                                                                         //直接堆到2026年，逻辑自动判断不需要的
    static public LocalDate firstMonday = LocalDate.of(2024, 2, 26);
    static public String currentTerm = "2023-2024-2";
    static public String[] termGroupString =
            {
                    "2023-2024-2",
                    "2024-2025-1",
                    "2024-2025-2",
                    "2025-2026-1",
                    "2025-2026-2"
            };
    static public LocalDate[] termStarts = {
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 9, 1),
            LocalDate.of(2025, 3, 1),
            LocalDate.of(2025, 9, 1),
            LocalDate.of(2026, 3, 1)
    };
    static public LocalDate[] termEnds = {
            LocalDate.of(2024, 8, 31),
            LocalDate.of(2025, 2, 28),
            LocalDate.of(2025, 8, 31),
            LocalDate.of(2026, 2, 28),
            LocalDate.of(2027, 8, 31)
    };
}
