package com.sensecolor.app.domain

import com.sensecolor.app.util.ColorUtils

/**
 * Core color naming engine. Converts RGB values into two tiers of color names:
 *   - Tier 1: Broad color family (e.g. "Red", "Blue", "Green")
 *   - Tier 2: Specific named color via nearest-neighbor HSL lookup (e.g. "Crimson", "Sapphire")
 */
object ColorNameMapper {

    private data class NamedColor(val name: String, val h: Float, val s: Float, val l: Float)

    // ── Tier 2 dictionary (150+ named colors with pre-computed HSL) ─────────

    private val namedColors: List<NamedColor> = listOf(
        // ── Reds ────────────────────────────────────────────────────────────
        NamedColor("Crimson",          348f, 0.83f, 0.47f),
        NamedColor("Scarlet",            0f, 1.00f, 0.42f),
        NamedColor("Ruby",             338f, 0.56f, 0.38f),
        NamedColor("Brick Red",          0f, 0.56f, 0.36f),
        NamedColor("Maroon",             0f, 1.00f, 0.25f),
        NamedColor("Rust",              16f, 0.69f, 0.35f),
        NamedColor("Vermilion",          6f, 0.89f, 0.52f),
        NamedColor("Tomato Red",         9f, 1.00f, 0.64f),
        NamedColor("Cherry",           346f, 0.74f, 0.33f),
        NamedColor("Fire Engine Red",    0f, 0.86f, 0.47f),
        NamedColor("Cardinal",         350f, 0.72f, 0.38f),
        NamedColor("Wine",             345f, 0.40f, 0.30f),
        NamedColor("Blood Red",        360f, 1.00f, 0.30f),
        NamedColor("Garnet",           350f, 0.56f, 0.32f),

        // ── Oranges / Warm tones ────────────────────────────────────────────
        NamedColor("Coral",             16f, 1.00f, 0.66f),
        NamedColor("Salmon",            17f, 0.77f, 0.71f),
        NamedColor("Peach",             28f, 0.87f, 0.77f),
        NamedColor("Apricot",           25f, 0.82f, 0.69f),
        NamedColor("Tangerine",         24f, 0.98f, 0.53f),
        NamedColor("Burnt Orange",      21f, 0.78f, 0.44f),
        NamedColor("Pumpkin",           24f, 0.99f, 0.47f),
        NamedColor("Amber",             36f, 1.00f, 0.49f),
        NamedColor("Marigold",          39f, 0.95f, 0.52f),
        NamedColor("Gold",              51f, 1.00f, 0.50f),
        NamedColor("Papaya",            20f, 0.90f, 0.62f),
        NamedColor("Persimmon",         14f, 0.86f, 0.52f),
        NamedColor("Copper",            20f, 0.64f, 0.44f),
        NamedColor("Bronze",            36f, 0.44f, 0.39f),
        NamedColor("Terracotta",        16f, 0.53f, 0.45f),

        // ── Yellows ─────────────────────────────────────────────────────────
        NamedColor("Lemon",             54f, 1.00f, 0.50f),
        NamedColor("Canary Yellow",     56f, 1.00f, 0.65f),
        NamedColor("Mustard",           47f, 0.75f, 0.45f),
        NamedColor("Saffron",           45f, 0.96f, 0.54f),
        NamedColor("Butter",            50f, 0.82f, 0.78f),
        NamedColor("Goldenrod",         43f, 0.74f, 0.49f),
        NamedColor("Flax",              50f, 0.58f, 0.69f),
        NamedColor("Dandelion",         47f, 0.97f, 0.56f),

        // ── Yellow-Greens / Chartreuse ──────────────────────────────────────
        NamedColor("Lime",              75f, 1.00f, 0.50f),
        NamedColor("Chartreuse",        90f, 1.00f, 0.50f),
        NamedColor("Spring Green",     150f, 1.00f, 0.50f),
        NamedColor("Yellow Green",      80f, 0.61f, 0.50f),
        NamedColor("Pear",              83f, 0.47f, 0.55f),

        // ── Greens ──────────────────────────────────────────────────────────
        NamedColor("Mint",             150f, 1.00f, 0.75f),
        NamedColor("Sage",             135f, 0.24f, 0.52f),
        NamedColor("Olive",             60f, 1.00f, 0.25f),
        NamedColor("Forest Green",     120f, 0.61f, 0.34f),
        NamedColor("Emerald",          140f, 0.68f, 0.44f),
        NamedColor("Kelly Green",      120f, 1.00f, 0.29f),
        NamedColor("Hunter Green",     120f, 1.00f, 0.19f),
        NamedColor("Sea Green",        146f, 0.50f, 0.36f),
        NamedColor("Jade",             152f, 0.56f, 0.46f),
        NamedColor("Pine",             134f, 0.43f, 0.28f),
        NamedColor("Moss",              85f, 0.31f, 0.35f),
        NamedColor("Fern",             113f, 0.40f, 0.42f),
        NamedColor("Shamrock",         140f, 0.62f, 0.40f),
        NamedColor("Pistachio",         96f, 0.46f, 0.65f),
        NamedColor("Basil",            119f, 0.50f, 0.30f),
        NamedColor("Clover",           120f, 0.47f, 0.32f),
        NamedColor("Juniper",          155f, 0.35f, 0.35f),
        NamedColor("Malachite",        135f, 0.73f, 0.46f),
        NamedColor("Seafoam",          153f, 0.52f, 0.67f),
        NamedColor("Pickle",            85f, 0.45f, 0.30f),

        // ── Teals ───────────────────────────────────────────────────────────
        NamedColor("Teal",             180f, 1.00f, 0.25f),
        NamedColor("Dark Teal",        180f, 0.80f, 0.20f),
        NamedColor("Verdigris",        173f, 0.48f, 0.46f),

        // ── Cyans / Aquas ───────────────────────────────────────────────────
        NamedColor("Turquoise",        174f, 0.72f, 0.56f),
        NamedColor("Aquamarine",       160f, 1.00f, 0.75f),
        NamedColor("Cyan",             180f, 1.00f, 0.50f),
        NamedColor("Aqua",             180f, 0.78f, 0.58f),
        NamedColor("Robin Egg Blue",   187f, 0.73f, 0.63f),

        // ── Blues ────────────────────────────────────────────────────────────
        NamedColor("Cerulean",         196f, 0.74f, 0.50f),
        NamedColor("Sky Blue",         197f, 0.71f, 0.73f),
        NamedColor("Powder Blue",      187f, 0.52f, 0.80f),
        NamedColor("Baby Blue",        199f, 0.54f, 0.78f),
        NamedColor("Steel Blue",       207f, 0.44f, 0.49f),
        NamedColor("Cornflower Blue",  219f, 0.79f, 0.66f),
        NamedColor("Royal Blue",       225f, 0.73f, 0.57f),
        NamedColor("Navy Blue",        240f, 1.00f, 0.25f),
        NamedColor("Midnight Blue",    240f, 0.64f, 0.27f),
        NamedColor("Cobalt",           215f, 1.00f, 0.44f),
        NamedColor("Sapphire",         219f, 0.56f, 0.42f),
        NamedColor("Denim",            218f, 0.39f, 0.53f),
        NamedColor("Periwinkle",       226f, 0.56f, 0.72f),
        NamedColor("Azure",            210f, 1.00f, 0.50f),
        NamedColor("Ocean Blue",       212f, 0.68f, 0.43f),
        NamedColor("Ultramarine",      232f, 0.80f, 0.42f),
        NamedColor("Prussian Blue",    209f, 0.85f, 0.23f),
        NamedColor("Columbia Blue",    205f, 0.58f, 0.73f),
        NamedColor("Ice Blue",         198f, 0.44f, 0.83f),
        NamedColor("Dodger Blue",      210f, 1.00f, 0.56f),

        // ── Indigos ─────────────────────────────────────────────────────────
        NamedColor("Indigo",           275f, 1.00f, 0.25f),
        NamedColor("Blue Violet",      271f, 0.76f, 0.53f),
        NamedColor("Iris",             264f, 0.45f, 0.52f),

        // ── Purples / Violets ───────────────────────────────────────────────
        NamedColor("Violet",           282f, 0.76f, 0.47f),
        NamedColor("Lavender",         240f, 0.67f, 0.94f),
        NamedColor("Lilac",            283f, 0.35f, 0.72f),
        NamedColor("Plum",             300f, 0.47f, 0.35f),
        NamedColor("Eggplant",         295f, 0.51f, 0.24f),
        NamedColor("Amethyst",         270f, 0.50f, 0.60f),
        NamedColor("Orchid",           302f, 0.59f, 0.65f),
        NamedColor("Mauve",            308f, 0.24f, 0.62f),
        NamedColor("Grape",            288f, 0.58f, 0.35f),
        NamedColor("Heather",          270f, 0.22f, 0.60f),
        NamedColor("Wisteria",         275f, 0.36f, 0.63f),
        NamedColor("Thistle",          300f, 0.24f, 0.80f),
        NamedColor("Royal Purple",     267f, 0.72f, 0.40f),
        NamedColor("Mulberry",         318f, 0.51f, 0.38f),

        // ── Magentas / Pinks ────────────────────────────────────────────────
        NamedColor("Magenta",          300f, 1.00f, 0.50f),
        NamedColor("Fuchsia",          300f, 1.00f, 0.47f),
        NamedColor("Hot Pink",         330f, 1.00f, 0.71f),
        NamedColor("Rose",             340f, 0.60f, 0.55f),
        NamedColor("Blush",            345f, 0.44f, 0.78f),
        NamedColor("Dusty Rose",       345f, 0.30f, 0.56f),
        NamedColor("Raspberry",        336f, 0.73f, 0.43f),
        NamedColor("Bubblegum",        330f, 0.75f, 0.73f),
        NamedColor("Flamingo",         343f, 0.85f, 0.68f),
        NamedColor("Cerise",           322f, 0.76f, 0.45f),
        NamedColor("Carnation",        339f, 0.65f, 0.62f),
        NamedColor("Pink",             350f, 1.00f, 0.88f),
        NamedColor("Watermelon",       352f, 0.72f, 0.52f),
        NamedColor("Strawberry",       348f, 0.78f, 0.46f),
        NamedColor("Rouge",            345f, 0.66f, 0.42f),
        NamedColor("Petal Pink",       340f, 0.55f, 0.85f),

        // ── Browns ──────────────────────────────────────────────────────────
        NamedColor("Chocolate",         25f, 0.75f, 0.27f),
        NamedColor("Sienna",            19f, 0.56f, 0.40f),
        NamedColor("Mahogany",           0f, 0.52f, 0.28f),
        NamedColor("Chestnut",          10f, 0.44f, 0.36f),
        NamedColor("Cinnamon",          25f, 0.61f, 0.35f),
        NamedColor("Caramel",           30f, 0.57f, 0.42f),
        NamedColor("Tan",               34f, 0.44f, 0.69f),
        NamedColor("Khaki",             54f, 0.77f, 0.75f),
        NamedColor("Sand",              39f, 0.46f, 0.68f),
        NamedColor("Wheat",             39f, 0.77f, 0.83f),
        NamedColor("Umber",             24f, 0.38f, 0.30f),
        NamedColor("Sepia",             26f, 0.45f, 0.33f),
        NamedColor("Espresso",          20f, 0.52f, 0.18f),
        NamedColor("Coffee",            30f, 0.35f, 0.28f),
        NamedColor("Mocha",             25f, 0.30f, 0.34f),
        NamedColor("Hickory",           22f, 0.44f, 0.32f),
        NamedColor("Tawny",             30f, 0.56f, 0.44f),
        NamedColor("Cocoa",             20f, 0.25f, 0.31f),

        // ── Neutrals / Whites / Creams ──────────────────────────────────────
        NamedColor("Cream",             40f, 1.00f, 0.94f),
        NamedColor("Ivory",             60f, 1.00f, 0.97f),
        NamedColor("Linen",             30f, 0.67f, 0.94f),
        NamedColor("Beige",             34f, 0.22f, 0.77f),
        NamedColor("Vanilla",           46f, 0.67f, 0.87f),
        NamedColor("Eggshell",          36f, 0.33f, 0.92f),
        NamedColor("Champagne",         36f, 0.50f, 0.84f),
        NamedColor("Bisque",            33f, 1.00f, 0.88f),

        // ── Grays / Blacks / Whites ─────────────────────────────────────────
        NamedColor("Charcoal",           0f, 0.00f, 0.21f),
        NamedColor("Slate",            210f, 0.13f, 0.50f),
        NamedColor("Ash",                0f, 0.00f, 0.46f),
        NamedColor("Silver",             0f, 0.00f, 0.75f),
        NamedColor("Platinum",           0f, 0.00f, 0.89f),
        NamedColor("Pearl",             50f, 0.18f, 0.92f),
        NamedColor("Snow",               0f, 1.00f, 0.99f),
        NamedColor("Onyx",             300f, 0.02f, 0.15f),
        NamedColor("Jet",                0f, 0.00f, 0.13f),
        NamedColor("Smoke",              0f, 0.00f, 0.56f),
        NamedColor("Graphite",         240f, 0.03f, 0.25f),
        NamedColor("Iron",               0f, 0.00f, 0.33f),
        NamedColor("Pewter",           150f, 0.07f, 0.60f),
        NamedColor("Fog",                0f, 0.00f, 0.84f),
        NamedColor("Nickel",             0f, 0.00f, 0.51f),
        NamedColor("Tungsten",           0f, 0.00f, 0.28f)
    )

    /**
     * Map an RGB color to a broad family name (tier 1) and a specific named
     * color (tier 2) via nearest-neighbor HSL distance.
     *
     * @return Pair of (tier1Name, tier2Name)
     */
    fun mapColor(r: Int, g: Int, b: Int): Pair<String, String> {
        val (h, s, l) = ColorUtils.rgbToHsl(r, g, b)
        val tier1 = tier1Family(h, s, l)
        val tier2 = tier2NearestName(h, s, l)
        return Pair(tier1, tier2)
    }

    // ── Tier 1: broad family classification ─────────────────────────────────

    private fun tier1Family(h: Float, s: Float, l: Float): String {
        // --- Achromatic checks first ---
        if (s < 0.10f && l > 0.85f) return "White"
        if (l < 0.12f) return "Black"
        if (s < 0.10f && l in 0.12f..0.85f) return "Gray"

        // --- Chromatic: special composites ---
        // Brown: warm hue, moderate saturation, dark-to-mid lightness
        if (h in 10f..45f && s in 0.15f..0.75f && l in 0.10f..0.42f) return "Brown"

        // Beige: warm hue, low saturation, light
        if (h in 30f..55f && s in 0.10f..0.40f && l in 0.70f..0.90f) return "Beige"

        // Light pink override: high lightness in pink hue range
        if (l > 0.75f && h in 330f..346f) return "Pink"

        // --- Hue-based buckets ---
        return when {
            h < 10f || h >= 346f -> "Red"
            h < 40f              -> "Orange"
            h < 65f              -> "Yellow"
            h < 80f              -> "Chartreuse"
            h < 165f             -> "Green"
            h < 185f             -> "Teal"
            h < 200f             -> "Cyan"
            h < 255f             -> "Blue"
            h < 275f             -> "Indigo"
            h < 310f             -> "Purple"
            h < 330f             -> "Magenta"
            else                 -> "Pink"
        }
    }

    // ── Tier 2: nearest named color by HSL distance ─────────────────────────

    private fun tier2NearestName(h: Float, s: Float, l: Float): String {
        var bestName = namedColors.first().name
        var bestDist = Float.MAX_VALUE

        for (nc in namedColors) {
            val d = ColorUtils.hslDistance(h, s, l, nc.h, nc.s, nc.l)
            if (d < bestDist) {
                bestDist = d
                bestName = nc.name
            }
        }

        return bestName
    }
}
