<template>
<div>
    <div class="plan etc">
        <div class="plan-head">
            <div id="name">
                {{ plan['name'] }}
                <p>{{ toAMPMString(plan['reservationDate']) }}</p>
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
              