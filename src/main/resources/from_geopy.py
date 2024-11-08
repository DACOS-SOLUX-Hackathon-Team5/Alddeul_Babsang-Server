from geopy.distance import geodesic
import pandas as pd
import sys
import json

sys.stdout.reconfigure(encoding="utf-8")


# 같은 cluster2에 있는 가장 가까운 4개의 가게를 찾는 함수
def find_nearest_stores(
    df_recommended, input_lat, input_long, target_cluster, input_storeId, num_stores=4
):
    # 동일한 클러스터에 있는 가게들 필터링 (본인 제외)
    same_cluster_data = df_recommended[
        (df_recommended["cluster2"] == target_cluster)
        & (df_recommended["업소아이디"] != input_storeId)
    ].copy()

    # 거리 계산 후 정렬
    same_cluster_data["distance"] = same_cluster_data.apply(
        lambda row: geodesic(
            (input_lat, input_long), (row["latitude"], row["longitude"])
        ).meters,
        axis=1,
    )
    nearest_stores = same_cluster_data.sort_values(by="distance").head(num_stores)

    return nearest_stores[["name"]]  # 가게 이름만 반환


# 데이터프레임 로드
df_recommended = pd.read_csv(
    r"C:\Users\82107\Desktop\Alddeul_Babsang-Server\src\main\resources\df_recommended (1).csv"
)

# 명령행 인수에서 위도, 경도, 클러스터 ID, 업소 ID 받기
input_lat = float(sys.argv[1])
input_long = float(sys.argv[2])
target_cluster = int(sys.argv[3])
input_storeId = int(sys.argv[4])

# 가까운 가게 찾기
nearest_stores = find_nearest_stores(
    df_recommended, input_lat, input_long, target_cluster, input_storeId
)

# 가게 이름만 리스트로 변환
store_names = nearest_stores["name"].tolist()  # 가게 이름만 리스트로 변환

# 가게 이름 출력
for store in store_names:
    print(store)  # 각 가게 이름을 줄 단위로 출력
