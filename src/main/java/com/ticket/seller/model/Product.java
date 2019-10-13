package com.ticket.seller.model;

import jdk.jshell.Snippet;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
        private String id;
        private String date;
        private String name;
        private String sellerId;
        private String image;
        private ProductCategory category;
        private ProductSubCategory subCategory;
        private String info;
        private ProductArea area;
        private Long price;
        private String option;

        public enum ProductCategory {
                LEISURE("레져"),
                EXHIBITION("전시"),
                TOUR("여행"),
                CONCERT("콘서트"),
                THEATER("연극");

                private String value;
                ProductCategory(String category) {
                        this.value = category;
                }

                public String getKey() { return name(); }
                public String getValue() { return value; }
        }
        public enum ProductSubCategory {
                SEA("해양", ProductCategory.LEISURE),
                LAND("육상", ProductCategory.LEISURE),
                SPA("스파", ProductCategory.LEISURE),

                EXPO("박람회", ProductCategory.EXHIBITION),
                DISPLAY("전시회", ProductCategory.EXHIBITION),
                MUSEUM("박물관", ProductCategory.EXHIBITION),

                FESTIVAL("축제", ProductCategory.TOUR),
                ATTRACTION("명소", ProductCategory.TOUR),

                TROT("트로트", ProductCategory.CONCERT),
                ROCK("락", ProductCategory.CONCERT),
                HIPHOPNRAP("힙합/랩", ProductCategory.CONCERT),
                BALLAD("발라드", ProductCategory.CONCERT),
                IDOL("아이돌", ProductCategory.CONCERT),
                INDE("인디", ProductCategory.CONCERT),
                OVERSEA("내한", ProductCategory.CONCERT),

                SMALLHALL("소극장", ProductCategory.THEATER),
                MUSICAL("뮤지컬", ProductCategory.THEATER),
                THEATERETC("기타", ProductCategory.THEATER);

                private String value;
                private ProductCategory parent;

                ProductSubCategory(String subCategory, ProductCategory parent) {
                        this.value = subCategory;
                        this.parent = parent;
                }
                public String getKey() {
                        return name();
                }
                public ProductCategory getParentProductCategory() { return parent; }
                public String getValue() {
                        return value;
                }
        }

        public enum ProductArea {
                SEOUL("서울"),
                INCHEON("인천"),
                DAEGU("대구"),
                DAEJEON("대전"),
                BUSAN("부산"),
                ULSAN("울산"),
                GWANGJU("광주"),
                GYEONGGI("경기"),
                GANGWON("강원"),
                CHUNGCHEONG("충청"),
                JEOLLA("전라"),
                GYEONGSANG("경상"),
                JEJU("제주");

                private String value;
                ProductArea(String value) {
                        this.value = value;
                }
                public String getKey() { return name(); }
                public String getValue() { return value; }
        }
}
