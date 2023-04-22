<template>
<div>
    <div class="plan">
        <div class="plan-head">
            <div id="departLocation">
                {{plan['departLocation']}}
                <p>{{ toAMPMString(plan['departDate']) }}</p>
            </div>
            <div id="progress-ico">
                <font-awesome-icon icon="fa-solid fa-jet-fighter" id="plane-img" />
            </div>
            <div id="arriveLocation">
                {{plan['arriveLocation']}}
                <p>{{ toAMPMString(plan['arriveDate']) }}</p>
            </div>
        </div>
        <div class="plan-contents">
            <table>
                <thead>
                    <tr>
                        <th>편명</th>
                        <th>좌석</th>
                        <th>항공사</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['flightNumber'] }}</td>
                        <td>{{ plan['seatNumber'] }}</td>
                        <td>{{ plan['airline'] }}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <th>탑승 시간</th>
                        <th>탑승구</th>
                        <th>터미널</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ toAMPMString(plan['boardingTime']).split('AM ')[1] }}</td>
                        <td>{{ plan['boardingGate'] }}</td>
                        <td>{{ plan['terminal'] }}</td>
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
                        <th>메모</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{{ plan['memo'] }}</td>
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
import { toAMPMString } from "../../../composable/util";

const props = defineProps({
    modelValue: Object,
});
const emit = defineEmits(['update:modelValue'])

const plan = computed(()=>props.modelValue||{})

</script>
          