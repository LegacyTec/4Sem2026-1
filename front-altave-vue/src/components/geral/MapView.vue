<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, watch } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

delete (L.Icon.Default.prototype as any)._getIconUrl
L.Icon.Default.mergeOptions({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})

const props = defineProps<{
  lat: number
  lng: number
  height?: string
  zoom?: number
  label?: string
}>()

const MAP_ID = `mapview-${Math.random().toString(36).slice(2)}`

let map: L.Map | null = null
let marker: L.Marker | null = null

function atualizarMarcador() {
  if (!map) return
  if (marker) marker.remove()
  marker = L.marker([props.lat, props.lng]).addTo(map)
  if (props.label) marker.bindPopup(props.label)
  map.setView([props.lat, props.lng], props.zoom ?? 14)
  nextTick(() => map?.invalidateSize())
}

onMounted(async () => {
  await nextTick()
  const el = document.getElementById(MAP_ID)
  if (!el) return

  map = L.map(el, { zoomControl: true, dragging: true, scrollWheelZoom: false })
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    maxZoom: 19,
  }).addTo(map)

  atualizarMarcador()
})

watch(
  () => [props.lat, props.lng, props.label],
  () => atualizarMarcador(),
)

onUnmounted(() => {
  map?.remove()
  map = null
  marker = null
})
</script>

<template>
  <div class="map-view-root">
    <div :id="MAP_ID" class="map-container" :style="{ height: height ?? '220px' }" />
    <p class="map-hint">
      <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
        <circle cx="12" cy="10" r="3" />
      </svg>
      {{ label || 'Localização da planta' }}
      · {{ lat.toFixed(5) }}, {{ lng.toFixed(5) }}
    </p>
  </div>
</template>

<style scoped>
.map-view-root {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.map-container {
  width: 100%;
  border-radius: var(--radius-md, 8px);
  border: 1px solid var(--gray-300);
  overflow: hidden;
  z-index: 0;
}

.map-hint {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 11px;
  color: var(--gray-500);
  margin: 0;
  flex-wrap: wrap;
}
</style>
