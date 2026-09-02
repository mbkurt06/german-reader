package de.bascurt.almancaokuyucu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.bascurt.almancaokuyucu.model.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF169C9C))) { DemoReader() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun DemoReader() {
    val unit = LearningUnit(
        id = "u1", base = "sich versammeln", type = "reflexive_verb",
        meaningTr = "toplanmak, bir araya gelmek",
        noteTr = "sich tek başına çevrilmez; fiille birlikte öğrenilir.",
        grammar = "Perfekt: hat sich versammelt", ranges = emptyList(),
        examples = listOf(Example("Die Schüler versammeln sich.", "Öğrenciler toplanıyor."))
    )
    var selected by remember { mutableStateOf<LearningUnit?>(null) }
    Scaffold(topBar = { TopAppBar(title = { Text("Almanca Okuyucu") }) }) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("B2") })
                Text("Hikâye", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Text("In Nebraska versammeln sich jedes Jahr über eine halbe Million Kraniche.", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { selected = unit }, shape = RoundedCornerShape(12.dp)) { Text("sich / versammeln yapısını aç") }
            Text("Metindeki iki ayrı parçaya dokunulduğunda aynı öğrenme kartı açılır.", color = Color.Gray)
        }
    }
    selected?.let { value ->
        ModalBottomSheet(onDismissRequest = { selected = null }) {
            Column(Modifier.padding(24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(value.base, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(value.meaningTr, style = MaterialTheme.typography.titleMedium)
                value.grammar?.let { Text(it) }
                value.noteTr?.let { Text(it) }
                value.examples.forEach { Text("${it.de}\n${it.tr}") }
                Button(onClick = { selected = null }) { Text("Kaydet") }
            }
        }
    }
}
