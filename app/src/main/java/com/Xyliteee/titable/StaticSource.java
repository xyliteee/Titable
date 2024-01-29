package com.Xyliteee.titable;

import java.time.LocalDate;

public class StaticSource {                                                                         //一些需要手动更新的静态资源
    static public LocalDate firstMonday = LocalDate.of(2024, 2, 26);
    static public String[] termGroupString =
            {
                    "2020-2021-1",
                    "2020-2021-2",
                    "2021-2022-1",
                    "2021-2022-2",
                    "2022-2023-1",
                    "2022-2023-2",
                    "2023-2024-1",
                    "2023-2024-2"
            };
    static public LocalDate[] termStarts = {
            LocalDate.of(2020, 9, 1),
            LocalDate.of(2021, 3, 1),
            LocalDate.of(2021, 9, 1),
            LocalDate.of(2022, 3, 1),
            LocalDate.of(2022, 9, 1),
            LocalDate.of(2023, 3, 1),
            LocalDate.of(2023, 9, 1),
            LocalDate.of(2024, 3, 1)
    };
    static public LocalDate[] termEnds = {
            LocalDate.of(2021, 2, 28),
            LocalDate.of(2021, 8, 31),
            LocalDate.of(2022, 2, 28),
            LocalDate.of(2022, 8, 31),
            LocalDate.of(2023, 2, 28),
            LocalDate.of(2023, 8, 31),
            LocalDate.of(2024, 2, 29),
            LocalDate.of(2024, 8, 31),
    };
}
