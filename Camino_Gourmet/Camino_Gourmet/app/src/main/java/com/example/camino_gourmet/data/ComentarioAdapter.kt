import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.camino_gourmet.data.Comentario
import com.example.camino_gourmet.R

class ComentariosAdapter(private val comentarios: List<Comentario>) :
    RecyclerView.Adapter<ComentariosAdapter.ComentarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_comentario_adapter, parent, false)
        return ComentarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {

        val comentario = comentarios[position]
        holder.nombreComentario.text = comentario.nombre_completo
        holder.calificacionComentario.text = comentario.calificacion
        holder.fechaComentario.text = comentario.fecha
        holder.contenidoComentario.text = comentario.descripcion
    }

    override fun getItemCount(): Int = comentarios.size

    class ComentarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreComentario: TextView = itemView.findViewById(R.id.nombreComentario)
        val calificacionComentario: TextView = itemView.findViewById(R.id.calificacionComentario)
        val fechaComentario: TextView = itemView.findViewById(R.id.fechaComentario)
        val contenidoComentario: TextView = itemView.findViewById(R.id.contenidoComentario)
    }
}