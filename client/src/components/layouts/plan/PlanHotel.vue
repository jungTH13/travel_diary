<template>
<div>
    <div class="plan ph">
        <div class="plan-head">
            <div id="checkinDate">
                <p>체크인</p>
                {{DateToStringFormat1(new Date(plan['checkinDate']))}}
                <p>{{ toAMPMString(plan['checkinDate']) }}</p>
            </div>
            <div id="progress-ico">
                <font-awesome-icon icon="fa-solid fa-bed" />
            </div>
            <div id="checkoutDate">
                <p>체크아웃</p>
                {{DateToStringFormat1(new Date(plan['checkoutDate']))}}
                <p>{{ toAMPMString(plan['checkoutDate']) }}</p>
            </div>
        </div>
        <div class="plan-contents">
            <table>
                <thead>
                    <tr>
                        <th>호텔 이름</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['name'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>주소</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['address'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>예약 번호</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['reservationNumber'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>전화번호</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['phone'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>메모</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><div v-html="urlParse(plan['memo']?.replaceAll('\n','<br/>'))"></div></td>
                    </tr>
                </tbody>
            </table>

        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>
@import "../../../assets/scss/components/plan.scss";
</style>

<script setup>

import { computed, ref, watch } from "vue";
import { DateToStringFormat1, toAMPMString, urlParse } from "../../../composable/util";

const props = defineProps({
    modelValue: Object,
});
const emit = defineEmits(['update:modelValue'])

const plan = computed(()=>props.modelValue||{})

</script>
              