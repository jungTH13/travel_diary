<template>
<div>
    <div class="plan pt">
        <div class="plan-head">
            <div id="checkinDate">
                <p>출발지</p>
                {{plan['departLocation']}}
                <p>{{ toAMPMString(plan['departDate']) }}</p>
            </div>
            <div id="progress-ico">
                <font-awesome-icon icon="fa-solid fa-van-shuttle" />
            </div>
            <div id="checkoutDate">
                <p>도착지</p>
                {{plan['arriveLocation']}}
                <p>{{ toAMPMString(plan['arriveDate']) }}</p>
            </div>
        </div>
        <div class="plan-contents">
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
                        <th>플랫폼</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['boardingGate'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>좌석</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['seatNumber'] }}</td>
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
              