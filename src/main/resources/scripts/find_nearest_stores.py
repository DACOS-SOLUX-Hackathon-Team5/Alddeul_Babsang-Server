from geopy.distance import geodesic

# 같은 cluster2에 있는 가장 가까운 4개의 가게를 찾는 함수
def find_nearest_stores(df_recommended, menu, num_stores=4):
    # 입력 받은 가게 데이터에서 위도, 경도, 클러스터 값 가져오기
    input_lat = menu['latitude'].values[0]
    input_long = menu['longitude'].values[0]
    target_cluster = menu['cluster2'].values[0]

    # 동일한 클러스터에 있는 가게들 필터링 (본인 제외)
    same_cluster_data = df_recommended[(df_recommended['cluster2'] == target_cluster) &
                                       (df_recommended['업소아이디'] != menu['업소아이디'].values[0])].copy()

    # 거리 계산 후 정렬
    same_cluster_data['distance'] = same_cluster_data.apply(
        lambda row: geodesic((input_lat, input_long), (row['latitude'], row['longitude'])).meters, axis=1
    )
    nearest_stores = same_cluster_data.sort_values(by='distance').head(num_stores)

    return nearest_stores[['업소아이디', '업소명', '구']]

find_nearest_stores(df_re,menu_example)