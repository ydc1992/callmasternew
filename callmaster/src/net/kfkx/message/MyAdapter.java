package net.kfkx.message;

/* import����class */
import java.util.List;

import net.kfkx.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* �۩w�q��Adapter�A�~��android.widget.BaseAdapter */
public class MyAdapter extends BaseAdapter
{
  /* �ܼƫŧi */
  private LayoutInflater mInflater;
  private List<String> items;
  private List<String> values;
  /* MyAdapter���غc�l�A�ǤJ�T�ӰѼ�  */  
  public MyAdapter(Context context,List<String> item,List<String> value)
  {
    /* �Ѽƪ�l�� */
    mInflater = LayoutInflater.from(context);
    items = item;
    values = value;
  }
  
  /* �]�~��BaseAdapter�A���мg�H�Umethod */
  public int getCount()
  {
    return items.size();
  }

  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  public long getItemId(int position)
  {
    return position;
  }
  
  public View getView(int position,View convertView,ViewGroup parent)
  {
    ViewHolder holder;
  
    if(convertView == null)
    {
      /* �ϥΦ۩w�q��file_row�@��Layout */
      convertView = mInflater.inflate(R.layout.row_layout,null);
      /* ��l��holder��text�Picon */
      holder = new ViewHolder();
      holder.text1=(TextView)convertView.findViewById(R.id.myText1);
      holder.text2=(TextView)convertView.findViewById(R.id.myText2);
    
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }
    /* �]�w�n��ܪ���T */
    holder.text1.setText(items.get(position).toString());
    holder.text2.setText(values.get(position).toString());
  
    return convertView;
  }
  
  /* class ViewHolder */
  private class ViewHolder
  {
    /* text1�G��T�W��
     * text2�G��T���e */
    TextView text1;
    TextView text2;
  }
}